package com.rtb.code_equivalent.service;

import com.rtb.code_equivalent.dto.CodeEquivalentResponseDTO;
import com.rtb.common.service.CommonService;
import com.rtb.code_equivalent.db.CodeEquivalent;
import com.rtb.code_equivalent.db.CodeEquivalentRepository;
import com.rtb.code_equivalent.dto.CodeEquivalentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.rtb.common.service.MarkdownService.getHtmlFromMarkdown;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeEquivalentService {

    private final CommonService commonService;
    private final CodeEquivalentRepository codeEquivalentRepository;

    @Transactional
    public String generateReport(CodeEquivalentDTO codeEquivalentDTO) {

        validateDTO(codeEquivalentDTO);

        // check in db first
        Optional<CodeEquivalent> dbResult = codeEquivalentRepository.findBySourceAndTargetAndTool(
                codeEquivalentDTO.getSource().trim().toLowerCase(),
                codeEquivalentDTO.getTarget().trim().toLowerCase(),
                codeEquivalentDTO.getTool().trim().toLowerCase()
        );

        if (dbResult.isPresent()) {
            log.info("CodeEquivalent already exists in DB");
            return getHtmlFromMarkdown(dbResult.get().getReportMarkdown());
        }

        String prompt = getPrompt(codeEquivalentDTO);

        log.info("Generating Report for: {}", codeEquivalentDTO);

        String result = commonService.getPromptTextResult(prompt);

        log.info("Report generated for: {}", codeEquivalentDTO);

        // save to db
        codeEquivalentRepository.save(new CodeEquivalent(
                null,
                codeEquivalentDTO.getSource().trim().toLowerCase(),
                codeEquivalentDTO.getTarget().trim().toLowerCase(),
                codeEquivalentDTO.getTool().trim().toLowerCase(),
                result
        ));

        return getHtmlFromMarkdown(result);
    }

    @Transactional(readOnly = true)
    public List<CodeEquivalentResponseDTO> getAllSavedReports() {
        return codeEquivalentRepository.findAllExceptReport();
    }

    @Transactional(readOnly = true)
    public String getReportById(Long id) {
        Optional<CodeEquivalent> dbResult = codeEquivalentRepository.findById(id);

        if (dbResult.isPresent()) {
            return getHtmlFromMarkdown(dbResult.get().getReportMarkdown());
        }else  {
            log.info("CodeEquivalent not found in DB");
            throw new IllegalArgumentException("CodeEquivalent not found in DB with id: " + id);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        codeEquivalentRepository.deleteById(id);
    }

    private void validateDTO(CodeEquivalentDTO codeEquivalentDTO) {

        if (codeEquivalentDTO == null
                || !StringUtils.hasText(codeEquivalentDTO.getSource())
                || !StringUtils.hasText(codeEquivalentDTO.getTarget())
                || !StringUtils.hasText(codeEquivalentDTO.getTarget())
        ) {
            throw new IllegalArgumentException("Please fill out all the fields");
        }
    }

    private String getPrompt(CodeEquivalentDTO codeEquivalentDTO) {

        return """
                # Role
                You are an expert software engineer with deep knowledge across multiple programming languages, frameworks, libraries, and development tools. You specialize in identifying equivalent implementations, best practices, and alternative approaches across different technology stacks.
                
                # Objective
                - Identify and provide the closest equivalent library, framework, tool, or technique in a target programming language that matches the functionality of a given implementation in a source language.
                - Clearly explain the similarities and differences between the source and target implementations, including any trade-offs or considerations.
                - When no direct equivalent exists, suggest alternative approaches or combinations of tools that can achieve similar functionality.
                - Provide practical guidance on how to implement or use the equivalent solution in the target language.
                - Generate output in a structured Markdown format that can be easily converted to HTML for display to end users.
                
                # Context
                Developers often work across multiple programming languages and need to translate their knowledge and tooling from one ecosystem to another. Different languages have different philosophies, conventions, and ecosystems, which means direct equivalents may not always exist. Your task is to bridge this gap by finding the best possible matches and explaining the landscape of options available. The output will be displayed in a web-based tool, so clarity and visual structure are important.
                
                # Instructions
                - When a user provides a source language with a specific library/framework/tool/technique and a target language, analyze the core functionality and purpose of the source implementation.
                - Search for the most widely-adopted and well-maintained equivalent in the target language that serves the same purpose.
                - **Always respond in the following structured Markdown format:**
                
                ## Output Format
                ````markdown
                # Language Equivalence Report
                
                ## Source Technology
                **Language:** [Source Language] \s
                **Library/Tool/Framework:** [Name] \s
                **Purpose:** [Brief description of what it does]
                
                ---
                
                ## Target Language
                **Language:** [Target Language]
                
                ---
                
                ## Equivalent Implementation
                
                ### Status
                [Choose one: ✅ Direct Equivalent Found | ⚠️ Partial Equivalent Found | ❌ No Direct Equivalent]
                
                ### Recommended Solution
                **Name:** [Library/Framework/Tool name or "Multiple approaches"] \s
                **Type:** [Library/Framework/Tool/Built-in Feature/Pattern] \s
                
                **Description:** \s
                [Brief description of the recommended solution]
                
                **Official Links:** \s
                - Documentation: [URL or "N/A"]
                - Repository: [URL or "N/A"]
                - Package Manager: [e.g., "npm install package-name" or "pip install package-name"]
                
                ---
                
                ## Comparison
                
                ### Similarities
                - [Similarity 1]
                - [Similarity 2]
                - [Similarity 3]
                
                ### Differences
                - [Difference 1]
                - [Difference 2]
                - [Difference 3]
                
                ### Trade-offs
                - [Trade-off 1]
                - [Trade-off 2]
                
                ---
                
                ## Usage Example
                
                ### Source ([Source Language])
                ```[source-language]
                [Code example showing usage in source language]
                ```
                
                ### Target ([Target Language])
                ```[target-language]
                [Equivalent code example in target language]
                ```
                
                ---
                
                ## Alternative Options
                
                [If applicable, list alternative approaches]
                
                ### Option 1: [Name]
                - **Description:** [Brief description]
                - **When to use:** [Use case]
                - **Link:** [URL or "N/A"]
                
                ### Option 2: [Name]
                - **Description:** [Brief description]
                - **When to use:** [Use case]
                - **Link:** [URL or "N/A"]
                
                ---
                
                ## Additional Notes
                [Any important considerations, caveats, or ecosystem differences]
                
                ---
                
                ## Community Adoption
                **Popularity:** [High/Medium/Low or specific metrics if available] \s
                **Maintenance Status:** [Active/Maintained/Legacy] \s
                **Maturity:** [Mature/Growing/Experimental]
                
                ---
                
                *Report generated on [Current Date]*
                ````
                
                - Follow this exact structure for every response. Do not deviate from the format.
                - If certain sections are not applicable (e.g., no usage example available), include the section header but state "N/A" or "Not applicable" in the content.
                - Use proper Markdown syntax including headers (#, ##, ###), bold (**text**), bullet points (-), code blocks (```language```), and horizontal rules (---).
                - Keep code examples concise but functional. Focus on the most common use case.
                - Ensure all URLs are valid and point to official sources when possible.
                - Use emojis (✅, ⚠️, ❌) for status indicators to improve visual clarity.
                - If multiple alternatives exist and no clear winner, set Status to "⚠️ Partial Equivalent Found" and provide detailed comparison in Alternative Options section.
                - If absolutely no equivalent exists, set Status to "❌ No Direct Equivalent" and focus on Alternative Options section with workarounds.
                - Be factual and objective. Avoid marketing language or bias toward specific tools.
                - The output should be ready to render as HTML without additional formatting.
                
                # Input:
                { "source": "%s", "target": "%s", "tool/framework/library": "%s" }
                """.formatted(codeEquivalentDTO.getSource(), codeEquivalentDTO.getTarget(), codeEquivalentDTO.getTool());
    }
}
