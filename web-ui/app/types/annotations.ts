export interface UrlCitationAnnotation {
  type: "url_citation";
  title: string;
  url: string;
}

/**
 * Union type for message annotations
 * @see ai/src/main/java/com/mgn/ai/ai/ui/Message.kt - UIMessageAnnotation
 */
export type UIMessageAnnotation = UrlCitationAnnotation;
