/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.model

sealed trait TemperatureUnit

object TemperatureUnit {

  case object Celsius extends TemperatureUnit

  case object Kelvin extends TemperatureUnit

  def fromString(unitAsString: String): TemperatureUnit = unitAsString match {
    case "K" => Kelvin
    case "C" => Celsius
    case _   => throw new IllegalArgumentException(s"unknown temperature unit $unitAsString")
  }

  def toString(unit: TemperatureUnit): String = unit match {
    case Kelvin  => "K"
    case Celsius => "C"
    case _       => throw new IllegalArgumentException(s"unknown temperature unit $unit")
  }

}
