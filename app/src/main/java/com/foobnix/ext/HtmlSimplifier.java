package com.foobnix.ext;

import com.foobnix.android.utils.LOG;
import com.foobnix.android.utils.TxtUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for simplifying HTML content by reversing preprocessing transformations
 * applied during EPUB processing, making it suitable for saving back to original files.
 *
 * This implementation focuses on:
 * - Hyphen removal (&shy; characters)
 * - Page marker cleanup (<pn> tags)
 * - Reference marker cleanup (<x-small> tags)
 * - HTML structure simplification (CSS to semantic markup)
 *
 * Excludes: text replacements, image conversion, footnotes (as per user requirements)
 */
public class HtmlSimplifier {

    private static final String SHY = "&shy;";
    private static final Pattern PAGE_MARKER_PATTERN = Pattern.compile("<br/><pn>page \\d+</pn><br/>", Pattern.CASE_INSENSITIVE);
    private static final Pattern REFERENCE_MARKER_PATTERN = Pattern.compile("<x-small>\\|\\d+\\.\\d+\\|</x-small>", Pattern.CASE_INSENSITIVE);

    /**
     * Simplifies HTML content by reversing preprocessing transformations
     * @param html The HTML content to simplify
     * @return Simplified HTML suitable for saving to original EPUB files
     */
    public static String simplifyHtml(String html) {
        if (TxtUtils.isEmpty(html)) {
            return html;
        }

        LOG.d("HtmlSimplifier", "Starting HTML simplification");

        String result = html;

        // 1. Remove soft hyphens
        result = removeHyphens(result);

        // 2. Remove page markers
        result = removePageMarkers(result);

        // 3. Remove reference markers
        result = removeReferenceMarkers(result);

        // 4. Simplify HTML markup (CSS styles to semantic markup)
        result = simplifyMarkup(result);

        LOG.d("HtmlSimplifier", "HTML simplification completed");
        return result;
    }

    /**
     * Removes soft hyphen characters (&shy;) from text content
     */
    private static String removeHyphens(String html) {
        if (!html.contains(SHY)) {
            return html;
        }

        LOG.d("HtmlSimplifier", "Removing hyphens");
        return html.replace(SHY, "");
    }

    /**
     * Removes page number markers (<br/><pn>page X</pn><br/>)
     */
    private static String removePageMarkers(String html) {
        Matcher matcher = PAGE_MARKER_PATTERN.matcher(html);
        if (!matcher.find()) {
            return html;
        }

        LOG.d("HtmlSimplifier", "Removing page markers");
        return matcher.replaceAll("");
    }

    /**
     * Removes reference markers (<x-small>|X.Y|</x-small>)
     */
    private static String removeReferenceMarkers(String html) {
        Matcher matcher = REFERENCE_MARKER_PATTERN.matcher(html);
        if (!matcher.find()) {
            return html;
        }

        LOG.d("HtmlSimplifier", "Removing reference markers");
        return matcher.replaceAll("");
    }

    /**
     * Simplifies HTML markup by converting CSS styles to semantic markup
     * Focuses on common patterns like italic styling
     */
    private static String simplifyMarkup(String html) {
        String result = html;

        // Convert common CSS italic styles to <i> tags
        result = convertCssItalicsToSemantic(result);

        // Convert common CSS bold styles to <strong> tags
        result = convertCssBoldToSemantic(result);

        // Remove unnecessary style attributes that are now semantic
        result = removeRedundantStyles(result);

        return result;
    }

    /**
     * Converts CSS italic styles to semantic <i> tags
     */
    private static String convertCssItalicsToSemantic(String html) {
        // Pattern for font-style: italic
        Pattern italicPattern = Pattern.compile("<[^>]+style=\"[^\"]*font-style:\\s*italic[^\"]*\"[^>]*>([^<]*)</[^>]+>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = italicPattern.matcher(html);

        if (matcher.find()) {
            LOG.d("HtmlSimplifier", "Converting CSS italics to semantic markup");
            return matcher.replaceAll("<i>$1</i>");
        }

        return html;
    }

    /**
     * Converts CSS bold styles to semantic <strong> tags
     */
    private static String convertCssBoldToSemantic(String html) {
        // Pattern for font-weight: bold
        Pattern boldPattern = Pattern.compile("<[^>]+style=\"[^\"]*font-weight:\\s*bold[^\"]*\"[^>]*>([^<]*)</[^>]+>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = boldPattern.matcher(html);

        if (matcher.find()) {
            LOG.d("HtmlSimplifier", "Converting CSS bold to semantic markup");
            return matcher.replaceAll("<strong>$1</strong>");
        }

        return html;
    }

    /**
     * Removes redundant style attributes after semantic conversion
     */
    private static String removeRedundantStyles(String html) {
        // Remove empty style attributes
        html = html.replaceAll(" style=\"\"", "");

        // Remove style attributes that only contained the converted properties
        html = html.replaceAll(" style=\"font-style:\\s*italic;?\\s*\"", "");
        html = html.replaceAll(" style=\"font-weight:\\s*bold;?\\s*\"", "");

        return html;
    }

    /**
     * Checks if HTML content contains any transformations that can be simplified
     * @param html The HTML content to check
     * @return true if simplifications are possible
     */
    public static boolean needsSimplification(String html) {
        if (TxtUtils.isEmpty(html)) {
            return false;
        }

        return html.contains(SHY) ||
               PAGE_MARKER_PATTERN.matcher(html).find() ||
               REFERENCE_MARKER_PATTERN.matcher(html).find() ||
               html.contains("font-style: italic") ||
               html.contains("font-weight: bold");
    }
}
