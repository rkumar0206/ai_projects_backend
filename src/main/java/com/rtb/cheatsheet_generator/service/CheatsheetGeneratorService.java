package com.rtb.cheatsheet_generator.service;

import com.rtb.cheatsheet_generator.db.CheatSheet;
import com.rtb.cheatsheet_generator.db.CheatSheetRepository;
import com.rtb.common.service.CommonService;
import com.rtb.common.service.MarkdownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheatsheetGeneratorService {

    private final CommonService commonService;
    private final CheatSheetRepository cheatSheetRepository;

    @Transactional
    public String generateCheatSheet(String technology) {

        // check in db first
        Optional<CheatSheet> dbResult = cheatSheetRepository.findByTechnology(technology.toLowerCase());
        if (dbResult.isPresent()) {
            log.info("CheatSheet found in db with technology {}", technology);
            return getHtmlFromMarkdown(dbResult.get().getCheatSheetMarkdown());
        }

        log.info("Generating Cheatsheet Sheet for: {}", technology);

        String prompt = cheatsheetGeneratorPrompt(technology);
        String result = commonService.getPromptTextResult(prompt);

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
                You are a senior-level technical writer, software architect, systems engineer, and domain expert capable of producing high-density, high-accuracy cheat sheets for any technology. You can explain programming languages, frameworks, libraries, databases, DevOps tools, cloud platforms, operating systems, networking concepts, and protocols with precision.
                
                #Objective:
                Generate a complete, deeply detailed, highly structured cheat sheet for any technology. The cheat sheet must function as a quick reference, a practical engineering companion, and an interview-focused summary. It must include fundamentals, advanced topics, examples, patterns, best practices, pitfalls, workflows, commands, tables, and architectural details.
                
                #Context:
                The cheat sheet must:
                - Render cleanly when converted from Markdown to HTML.
                - ALWAYS wrap tables, code samples, commands, configurations, and ASCII diagrams inside fenced code blocks (```), ensuring visual stability in HTML.
                - Keep headings, paragraphs, and bulleted lists OUTSIDE code blocks to ensure semantic HTML structure.
                - Maintain predictable formatting so downstream parsing and rendering remain stable.
                - Provide self-contained and production-relevant knowledge.
                - Adapt to technology type:
                  - Programming language → syntax, types, control flow, OOP, concurrency, idioms.
                  - Framework/library → core modules, lifecycle, configs, APIs, annotations.
                  - DevOps/cloud → commands, YAML, architecture, workflows, deployments.
                  - Database → DDL/DML, indexing, transactions, performance tuning.
                  - Networking/protocol → headers, message structures, flows, rules.
                - Avoid generic statements and marketing tone.
                - Produce deterministic, strict Markdown formatting.
                - Never include external references or links.
                
                #Instructions:
                - Do NOT wrap the whole output in a single fenced code block.
                - Use regular Markdown for all headings, sections, paragraphs, and lists.
                - Use fenced code blocks (```) ONLY for:
                  - Tables \s
                  - CLI commands \s
                  - Code snippets \s
                  - YAML/JSON configs \s
                  - ASCII diagrams \s
                  - Any formatted content that HTML might distort
                - Begin with a concise 2–3 sentence **Overview**.
                - Include a **Table of Contents**.
                - After that, create SECTIONS in the exact order below:
                
                  1. **Overview**
                  2. **Core Concepts**
                  3. **Quick Syntax / Commands / API Summary** \s
                     - All syntax tables, command tables, keyword tables MUST be inside ``` code blocks.
                  4. **Practical Examples** \s
                     - All examples must be inside code blocks.
                  5. **Architecture / Internal Model** \s
                     - ASCII diagrams inside code blocks.
                  6. **Workflow & Common Use Cases**
                  7. **Best Practices**
                  8. **Advanced Topics**
                     - Memory model \s
                     - Concurrency & parallelism \s
                     - Performance optimization \s
                     - Security principles \s
                     - Scaling strategies \s
                     - Deep-dive internals \s
                  9. **Troubleshooting & Common Pitfalls**
                  10. **Cheat Tables** \s
                      - All tables inside code blocks.
                  11. **Patterns / Design Patterns / Implementation Patterns**
                  12. **Interview Highlights**
                  13. **Version Differences / Migration Notes**
                
                - Ensure:
                  - All tables use `|` pipes and render correctly inside code blocks.
                  - All examples are syntactically valid.
                  - All sections contain meaningful, high-density content.
                  - No meta explanations or filler text appears in the output.
                  - Tone is technical, precise, and production-focused.
                
                #User Input:
                
                """ + technology;
    }
}
