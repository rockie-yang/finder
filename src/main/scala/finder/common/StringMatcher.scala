package finder.common

import scala.Predef._

//trait StringMatcher {
//  def apply(origin: String): Boolean
//}

trait StringPredicate {
  def apply(content: String): Boolean
}

class StringMatcher(predicate: (String, String) => Boolean,
                    ignoreCase: Boolean, matchStrings: List[String]) extends StringPredicate {
  val matchStrs = if (ignoreCase) matchStrings.map(_.toLowerCase) else matchStrings

  def apply(content: String): Boolean = {
    val contentStr =
      if (ignoreCase) content.toLowerCase
      else content

    matchStrs.forall(matchStr => predicate(contentStr, matchStr))
  }
}

object MatcherRules {

  def apply(rule: String, include: Boolean) = rule match {
    case "contain" =>
      if (include) (c: String, s: String) => c.contains(s)
      else (c: String, s: String) => c.contains(s)
    case _ => (c: String, s: String) => true
  }
}

object StringMatcher {

  def apply(rule: String,
            ignoreCase: Boolean, matchStrings: List[String]) = {

    val predicate = MatcherRules(rule, include = true)

    new StringMatcher(predicate, ignoreCase, matchStrings)
  }
}

object StringUnMatcher {

  def apply(rule: String,
            ignoreCase: Boolean, matchStrings: List[String]) = {

    val predicate = MatcherRules(rule, include = false)

    new StringMatcher(predicate, ignoreCase, matchStrings)
  }
}

class StringMatchers(matchers: StringMatcher*) extends StringPredicate {
  def apply(content: String): Boolean = {
    matchers.forall(amatch => amatch(content))
  }
}


