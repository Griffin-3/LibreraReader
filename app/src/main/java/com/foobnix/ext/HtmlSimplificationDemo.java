package com.foobnix.ext;

import com.foobnix.android.utils.LOG;

/**
 * Demonstration and testing utility for the simplified HTML hybrid approach
 * Shows how preprocessing transformations are reversed for clean EPUB saving
 */
public class HtmlSimplificationDemo {

    /**
     * Demonstrates the HTML simplification process with sample content
     */
    public static void demonstrateSimplification() {
        LOG.d("HtmlSimplificationDemo", "=== HTML Simplification Demo ===");

        // Sample HTML with various preprocessing transformations
        String sampleHtml = createSampleHtml();

        LOG.d("HtmlSimplificationDemo", "Original HTML length:", sampleHtml.length());
        LOG.d("HtmlSimplificationDemo", "Original HTML:");
        LOG.d("HtmlSimplificationDemo", sampleHtml);

        // Check if simplification is needed
        boolean needsSimplification = HtmlSimplifier.needsSimplification(sampleHtml);
        LOG.d("HtmlSimplificationDemo", "Needs simplification:", needsSimplification);

        if (needsSimplification) {
            // Apply simplification
            String simplifiedHtml = HtmlSimplifier.simplifyHtml(sampleHtml);

            LOG.d("HtmlSimplificationDemo", "Simplified HTML length:", simplifiedHtml.length());
            LOG.d("HtmlSimplificationDemo", "Simplified HTML:");
            LOG.d("HtmlSimplificationDemo", simplifiedHtml);

            // Show the differences
            showDifferences(sampleHtml, simplifiedHtml);
        } else {
            LOG.d("HtmlSimplificationDemo", "No simplification needed");
        }

        LOG.d("HtmlSimplificationDemo", "=== Demo Complete ===");
    }

    /**
     * Creates sample HTML content with typical preprocessing transformations
     */
    private static String createSampleHtml() {
        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<p>This is some text with hy&shy;phen&shy;ation that should be re&shy;moved.</p>");

        // Page marker
        html.append("<br/><pn>page 5</pn><br/>");

        // Reference marker
        html.append("<p><x-small>|1.2|</x-small> This is a reference.</p>");

        // CSS italic styling
        html.append("<p><span style=\"font-style: italic\">This is italic text</span> that should become semantic.</p>");

        // CSS bold styling
        html.append("<p><span style=\"font-weight: bold\">This is bold text</span> that should become semantic.</p>");

        // Mixed content
        html.append("<p>Normal text <span style=\"font-style: italic; font-weight: bold\">italic and bold</span> more normal text.</p>");

        html.append("</body></html>");

        return html.toString();
    }

    /**
     * Shows the differences between original and simplified HTML
     */
    private static void showDifferences(String original, String simplified) {
        LOG.d("HtmlSimplificationDemo", "=== Differences ===");

        String[] originalLines = original.split("\n");
        String[] simplifiedLines = simplified.split("\n");

        for (int i = 0; i < Math.max(originalLines.length, simplifiedLines.length); i++) {
            String origLine = i < originalLines.length ? originalLines[i] : "";
            String simpLine = i < simplifiedLines.length ? simplifiedLines[i] : "";

            if (!origLine.equals(simpLine)) {
                LOG.d("HtmlSimplificationDemo", "Line", (i + 1) + ":");
                LOG.d("HtmlSimplificationDemo", "  Original:  ", origLine);
                LOG.d("HtmlSimplificationDemo", "  Simplified:", simpLine);
            }
        }
    }

    /**
     * Tests individual simplification components
     */
    public static void testComponents() {
        LOG.d("HtmlSimplificationDemo", "=== Component Tests ===");

        // Test hyphen removal
        String withHyphens = "This is text with&shy;soft&shy;hyphens";
        String withoutHyphens = HtmlSimplifier.simplifyHtml(withHyphens);
        LOG.d("HtmlSimplificationDemo", "Hyphen test:");
        LOG.d("HtmlSimplificationDemo", "  Before:", withHyphens);
        LOG.d("HtmlSimplificationDemo", "  After: ", withoutHyphens);

        // Test page marker removal
        String withPageMarker = "Text<br/><pn>page 10</pn><br/>more text";
        String withoutPageMarker = HtmlSimplifier.simplifyHtml(withPageMarker);
        LOG.d("HtmlSimplificationDemo", "Page marker test:");
        LOG.d("HtmlSimplificationDemo", "  Before:", withPageMarker);
        LOG.d("HtmlSimplificationDemo", "  After: ", withoutPageMarker);

        // Test CSS to semantic conversion
        String withCssItalic = "<span style=\"font-style: italic\">Italic text</span>";
        String withSemanticItalic = HtmlSimplifier.simplifyHtml(withCssItalic);
        LOG.d("HtmlSimplificationDemo", "CSS to semantic test:");
        LOG.d("HtmlSimplificationDemo", "  Before:", withCssItalic);
        LOG.d("HtmlSimplificationDemo", "  After: ", withSemanticItalic);

        LOG.d("HtmlSimplificationDemo", "=== Component Tests Complete ===");
    }
}
