# Simplified Hybrid Approach for EPUB Save Operations

## Overview

This implementation provides a **simplified hybrid approach** for saving EPUB files with preprocessing transformations reversed, addressing the user's specific requirements while maintaining feasibility and reliability.

## User Requirements Addressed

### ✅ **Text Replacements: Forbidden**
- **Status**: Completely excluded from implementation
- **Reasoning**: As requested, text replacements are not handled since they will be forbidden
- **Implementation**: No reversal logic for `AppState.get().isEnableTextReplacement` transformations

### ✅ **Image Conversion: Not Interested**
- **Status**: Completely excluded from implementation
- **Reasoning**: As requested, image conversion (remote images, SVG/Math to PNG) is not handled
- **Implementation**: No reversal logic for `processRemoteImages()` or SVG/Math processing

### ✅ **HTML Markup Simplification: Required**
- **Status**: Implemented with semantic markup preservation
- **Implementation**: Converts CSS styles to semantic HTML (`<i>`, `<strong>`)
- **Example**: `<span style="font-style: italic">text</span>` → `<i>text</i>`

### ✅ **Footnotes: Not Critical**
- **Status**: Excluded from implementation
- **Reasoning**: As noted, footnotes appear in <2% of fiction and are not editable
- **Implementation**: No reversal logic for footnote integration

## Implemented Transformations

### 1. **Hyphen Removal** (High Confidence)
- **Transformation**: Removes `&shy;` (soft hyphen) characters
- **Reversibility**: ✅ **Fully Reversible**
- **Implementation**: Simple string replacement
- **Example**: `hy&shy;phen&shy;ated` → `hyphenated`

### 2. **Page Marker Cleanup** (High Confidence)
- **Transformation**: Removes `<br/><pn>page X</pn><br/>` markers
- **Reversibility**: ✅ **Fully Reversible**
- **Implementation**: Regex pattern matching and removal
- **Example**: `text<br/><pn>page 5</pn><br/>more` → `textmore`

### 3. **Reference Marker Cleanup** (High Confidence)
- **Transformation**: Removes `<x-small>|X.Y|</x-small>` markers
- **Reversibility**: ✅ **Fully Reversible**
- **Implementation**: Regex pattern matching and removal
- **Example**: `<x-small>|1.2|</x-small> text` → `text`

### 4. **HTML Structure Simplification** (Medium Confidence)
- **Transformation**: Converts CSS styles to semantic markup
- **Reversibility**: ✅ **Reversible for common patterns**
- **Implementation**: Pattern matching for `font-style: italic` and `font-weight: bold`
- **Examples**:
  - `font-style: italic` → `<i>text</i>`
  - `font-weight: bold` → `<strong>text</strong>`

## Implementation Architecture

### Core Classes

#### `HtmlSimplifier.java`
```java
public class HtmlSimplifier {
    public static String simplifyHtml(String html)
    public static boolean needsSimplification(String html)
    // Individual transformation methods...
}
```

#### `MuPdfDocument.java` (Enhanced)
```java
public String documentToSimplifiedHtml()
public boolean saveToOriginalWithSimplification(String originalPath)
```

#### `HtmlSimplificationDemo.java`
- Demonstration and testing utilities
- Sample content generation
- Difference visualization

### Integration Points

1. **HTML Extraction**: Uses existing `getPageHTML()` method
2. **Simplification**: Applies `HtmlSimplifier.simplifyHtml()` to each page
3. **Save Process**: Placeholder for EPUB ZIP manipulation (needs implementation)

## Feasibility Assessment

### ✅ **High Confidence Transformations**
- **Hyphen removal**: Simple, reliable, no edge cases
- **Page markers**: Pattern-based, consistent format
- **Reference markers**: Pattern-based, consistent format

### ⚠️ **Medium Confidence Transformations**
- **HTML simplification**: Depends on CSS patterns, may miss edge cases
- **Semantic conversion**: Limited to common italic/bold patterns

### ❌ **Excluded Transformations** (As Requested)
- **Text replacements**: Forbidden by user requirements
- **Image processing**: Not interested by user requirements
- **Footnotes**: Not critical for fiction, <2% occurrence

## Usage Example

```java
// Extract simplified HTML from document
MuPdfDocument doc = // ... load document
String simplifiedHtml = doc.documentToSimplifiedHtml();

// Save to original EPUB (placeholder implementation)
boolean success = doc.saveToOriginalWithSimplification("/path/to/original.epub");
```

## Benefits of This Approach

1. **Focused Scope**: Only handles reliably reversible transformations
2. **High Reliability**: Excludes complex/lossy transformations
3. **Performance**: Lightweight processing with simple string operations
4. **Maintainability**: Clear separation of concerns, easy to extend
5. **User-Aligned**: Directly addresses specific user requirements

## Future Enhancements

1. **EPUB Save Implementation**: Complete the ZIP manipulation logic
2. **Additional Semantic Patterns**: Extend CSS-to-HTML conversion
3. **Configuration Options**: Allow users to enable/disable specific simplifications
4. **Validation**: Add tests for edge cases and complex HTML structures

## Conclusion

This simplified hybrid approach provides a **practical and reliable solution** for saving EPUB files with preprocessing transformations reversed, while respecting the user's specific requirements and constraints. The implementation focuses on high-confidence transformations that can be reliably reversed, ensuring data integrity and user satisfaction.
