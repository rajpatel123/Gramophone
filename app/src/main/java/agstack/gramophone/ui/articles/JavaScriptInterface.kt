package agstack.gramophone.ui.articles

abstract class JavaScriptInterface {
    abstract fun goToProduct(productId: String)
    abstract fun openProduct(productId: String)
    abstract fun shareProduct(productId: String)
    abstract fun shareArticle(contentUrlString: String, contentTitle: String, imageUrlString: String, summaryEmbeddedTag: String)
}