package yuuki.content;

/**
 * Indicates that implementors can have content merged with them.
 * 
 * @param <E> The type of elements in content.
 */
public interface Mergeable<E> {
	
	/**
	 * Merges content with the current set of content. If this collection
	 * contains content that already exists in the current collection, it is
	 * replaced until it is subtracted.
	 * 
	 * @param content The content to be merged in with the existing content.
	 */
	public void merge(E content);
	
	/**
	 * Subtracts content from the current set of content. If content that was
	 * previously covering existing content is subtracted, the hidden content
	 * is then exposed.
	 * 
	 * @param content The content to be subtracted from the existing content.
	 */
	public void subtract(E content);
	
}
