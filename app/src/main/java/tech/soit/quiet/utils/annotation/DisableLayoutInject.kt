package tech.soit.quiet.utils.annotation

/**
 * annotated an activity which do not need [LayoutId] to create UI
 */
@Target(AnnotationTarget.CLASS)
annotation class DisableLayoutInject