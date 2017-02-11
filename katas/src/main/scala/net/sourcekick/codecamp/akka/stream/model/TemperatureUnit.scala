package net.sourcekick.codecamp.akka.stream.model

sealed trait TemperatureUnit

object TemperatureUnit {

  case object Celsius extends TemperatureUnit

  case object Kelvin extends TemperatureUnit

  def fromString(unitAsString: String): TemperatureUnit = unitAsString match {
    case "K" => Kelvin
    case "C" => Celsius
    case _ => throw new IllegalArgumentException(s"unknown temperature unit $unitAsString")
  }

}
