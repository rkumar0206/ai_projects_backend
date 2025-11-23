package com.rtb.cheatsheet_generator.service;

import com.rtb.cheatsheet_generator.db.CheatSheet;
import com.rtb.cheatsheet_generator.db.CheatSheetRepository;
import com.rtb.common.service.CommonService;
import com.rtb.common.service.MarkdownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheatsheetGeneratorService {

    private final CommonService commonService;
    private final CheatSheetRepository cheatSheetRepository;
    private final Set<String> invalidTechnologies = new HashSet<>();

    @Transactional
    public String generateCheatSheet(String technology) {

        if (invalidTechnologies.contains(technology.toLowerCase())) {
            throw new IllegalArgumentException("The technology entered is not valid.");
        }

        // check in db first
        Optional<CheatSheet> dbResult = cheatSheetRepository.findByTechnology(technology.toLowerCase());

        if (dbResult.isPresent()) {
            log.info("CheatSheet found in db with technology {}", technology);
            return getHtmlFromMarkdown(dbResult.get().getCheatSheetMarkdown());
        }

        log.info("Generating Cheatsheet Sheet for: {}", technology);

        String prompt = cheatsheetGeneratorPrompt(technology);
        String result = commonService.getPromptTextResult(prompt);

        if (result != null && result.startsWith("false")) {
            invalidTechnologies.add(technology.toLowerCase());
            throw new IllegalArgumentException("The technology entered is not valid.");
        }

        log.info("Generated Cheatsheet Sheet for: {}", technology);

        // save to db
        cheatSheetRepository.save(new CheatSheet(
                null,
                technology.toLowerCase(),
                result
        ));

        return getHtmlFromMarkdown(result);
    }

    private String getHtmlFromMarkdown(String cheatSheetMarkdown) {

        String html = sanitizeHtml(MarkdownService.markdownToHtml(cheatSheetMarkdown));

        return """
                <!DOCTYPE html>
                <html xmlns="http://www.w3.org/1999/xhtml">
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 20px; }
                        pre { background: #f5f5f5; padding: 10px; border-radius: 5px; }
                        code { font-family: monospace; }
                    </style>
                </head>
                <body>
                    %s
                </body>
                </html>
                """.formatted(html);
    }

    String sanitizeHtml(String html) {
        return html.replaceAll("<br>", "<br/>").replaceAll("<hr>", "<hr/>").replaceAll("<img(.*?)>", "<img$1/>");
    }

    private String cheatsheetGeneratorPrompt(String technology) {

        return """
                #Role:
                You are a senior-level technical writer, software architect, systems engineer, and domain expert capable of producing high-density, high-accuracy cheat sheets for any technology. You understand programming languages, frameworks, libraries, databases, DevOps tools, cloud platforms, operating systems, networking concepts, and protocols with precision.
                
                #Objective:
                Generate a complete, deeply detailed, highly structured cheat sheet for any technology. The cheat sheet must function as a quick reference guide, engineering companion, and interview-ready technical summary. It must include fundamentals, advanced insights, examples, patterns, best practices, workflows, tables, pitfalls, and architectural understanding.
                
                #Context:
                The cheat sheet must:
                - Render cleanly when converted from Markdown to HTML.
                - ALWAYS wrap tables, code samples, commands, configurations, and ASCII diagrams inside fenced code blocks (```), ensuring stable HTML rendering.
                - Keep headings, paragraphs, and bulleted lists OUTSIDE code blocks for clean semantic HTML.
                - Use deterministic, predictable Markdown formatting.
                - Adapt formatting and depth based on technology type.
                - Do not add fluff, generic statements, or external links.
                - Provide fully self-contained content that is production-relevant.
                
                #Instructions:
                - First validate the technology (User input) entered by user, if technology is not valid then just reply with false and nothing else.
                - Do NOT wrap the entire cheat sheet inside one code block.
                - Use fenced code blocks (```) ONLY for tables, code, configs, commands, and diagrams.
                - Begin with a concise 2–3 sentence **Overview**.
                - Include a **Table of Contents**.
                - Create the following sections in the exact order below:
                
                  1. **Overview**
                  2. **Core Concepts**
                  3. **Quick Syntax / Commands / API Summary**
                     - All syntax tables, command tables, keyword tables must be inside code blocks.
                  4. **Practical Examples**
                     - All examples must be inside code blocks.
                  5. **Architecture / Internal Model**
                     - ASCII diagrams must be inside code blocks.
                  6. **Workflow & Common Use Cases**
                  7. **Best Practices**
                  8. **Advanced Topics**
                     - The AI must intelligently choose advanced topics appropriate to the technology.
                     - These may include (but are not limited to): performance tuning, concurrency, memory handling, scaling techniques, security considerations, lifecycle mechanics, compilation model, protocol internals, or any deep technical aspects relevant to the given technology.
                     - Do NOT list fixed items; customize entirely based on the user input.
                  9. **Troubleshooting & Common Pitfalls**
                  10. **Cheat Tables**
                      - All tables must be inside code blocks.
                  11. **Patterns / Design Patterns / Implementation Patterns**
                  12. **Interview Highlights**
                  13. **Version Differences / Migration Notes** (if applicable)
                
                - Ensure:
                  - All tables use pipes (|) and render correctly inside code blocks.
                  - All code samples are valid and production-grade.
                  - Each section contains dense, meaningful information.
                  - Output contains no meta explanations.
                  - Tone is technical, precise, and directly useful.
                
                #User Input:
                
                """ + technology;
    }
}
