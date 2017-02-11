package net.sourcekick.codecamp.akka.stream.model

import java.time.Instant
import java.util.UUID

case class TemperatureReading(uuid: String, instant: Instant, temperature: Float, unit: TemperatureUnit) {
  require(!uuid.trim().isEmpty, "uuid be given")
  require(4 == UUID.fromString(uuid).version(), "uuid must be of RFC4122 type 4 random")
}
