// from scala.js 0.60 - remove when we upgrade to 0.6
 
package scala.scalajs.js.annotation

import scala.annotation.Annotation

class JSExportAll extends scala.annotation.Annotation
class JSExportDescendentObjects extends scala.annotation.Annotation
class JSExportDescendentClasses extends scala.annotation.Annotation

class JSExportNamed extends scala.annotation.Annotation {
  def this(name: String) = this()
}

class JSExport extends scala.annotation.Annotation {
  def this(name: String) = this()
}