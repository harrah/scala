package noshortcut

final class Key[T](val name: String) {
  def := (t: => T): Setting[T] = new Setting(this, Init.value(t))
  def := (i: Initialize[T]): Setting[T] = new Setting(this, i)
}

sealed trait Initialize[T]
private final class Value[T](val v: () => T) extends Initialize[T]

object Init
{
	def value[T](t: => T): Initialize[T] = new Value(t _ )
}

final class Setting[T](val k: Key[T], val i: Initialize[T])


object Use {
  val a = new Key[Int]("test")
  val b = new Key[List[Int]]("list")

  // invalid
  val s = a := true
  val t = a := Init.value { val x = false; x }
  // ok
  val g = a := 9
  val k = a := Init.value(9)
  val m = (new Key("infer")) := true
  val p = (new Key("infer")) := Init.value(9)

  // invalid
  val q = b := true
  val r = b := List(true)
  //.ok
  val h = b := List(3,4)
  val i = b := Init.value(List(5))
  val n = (new Key("infer")) := List(4,9)
  val c = (new Key("infer")) := Init.value(List(true))
}

