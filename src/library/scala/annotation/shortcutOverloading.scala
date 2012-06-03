package scala.annotation

/** Different overloading rules apply when a method has a single
* parameter with a type with this annotation.
*
* This different behavior is demonstrated by the following example:
*
*   class Normal[T](val t: T)
* 
*   @shortcutOverloading 
*   class Special[T](val t: T)
*
*   def overloaded[T](normal: Normal[T]) = ...
*   def overloaded[T](special: Special[T]) = ...
*
*   overloaded(new Normal(true))
*   overloaded(new Special(3))
*
* Normally, scalac will infer the type of `new Normal` and `new Special` using an undefined expected type
* because it doesn't know which overloaded variant to use.  This results in imprecise error messages
* and suboptimal type inference.  Because Special is annotated with @shortcutOverloading, however,
* scalac chooses the overloaded variant based on whether the inferred type of the argument is also 
* annotated with @shortcutOverloading instead of the usual subtype and specificity checks.  Then, it
* re-infers the type of the argument using the newly determined expected type.
*
* The effect of this is that specifying an incorrect type parameter still provides a good error message.
* For example, the following code has a type error.
*
* {{{
*   overloaded[Boolean](new Normal(3))
* }}}
*
* The error message displayed when Special has the shortcutOverloading annotation is:
* 
* {{{
*  A.scala:12: error: type mismatch;
*   found   : Int(3)
*   required: Boolean
*    overloaded[Boolean](new Normal(3))
*                                  ^
* }}}
*
* Without the shortcutOverloading annotation on Special,
*
* {{{
* A.scala:11: error: overloaded method value overloaded with alternatives:
*   (special: Special[Boolean])Unit <and>
*   (normal: Normal[Boolean])Unit
*  cannot be applied to (Normal[Int])
*   overloaded[Boolean](new Normal(3))
*             ^
*  }}}
*
* The example is contrived, but represents a common situation in practice.
* The type parameter isn't necessarily explicit, but it may be fixed by inference of other code.
*/
final class shortcutOverloading extends annotation.StaticAnnotation
