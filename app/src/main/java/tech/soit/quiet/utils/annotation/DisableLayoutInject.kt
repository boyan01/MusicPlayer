package tech.soit.quiet.utils.annotation

/**
 * annotated a fragment or activity which do not need [LayoutId] to create UI
 */
@Target(AnnotationTarget.CLASS)
annotation class DisableLayoutInject