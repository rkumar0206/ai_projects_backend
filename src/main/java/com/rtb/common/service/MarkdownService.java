package com.rtb.common.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MarkdownService {

    public static String markdownToHtml(String markdownText) {

        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        return renderer.render(parser.parse(markdownText));
    }

    public static String getHtmlFromMarkdown(String cheatSheetMarkdown) {

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

    private static String sanitizeHtml(String html) {
        return html.replaceAll("<br>", "<br/>").replaceAll("<hr>", "<hr/>").replaceAll("<img(.*?)>", "<img$1/>");
    }

    private MarkdownService() {}

}
