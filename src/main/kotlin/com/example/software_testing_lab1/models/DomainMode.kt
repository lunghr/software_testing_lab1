package com.example.software_testing_lab1.models

enum class Action {
    FLY, STAY
}

enum class WindowAppearance {
    MAJESTIC,
    ORDINARY,
    UNSIGHTLY
}

enum class Location {
    IN_FRONT_OF,
    BEHIND
}

fun toggleLocation(location: Location) = when (location) {
    Location.BEHIND -> Location.IN_FRONT_OF
    Location.IN_FRONT_OF -> Location.BEHIND
}

enum class CrowdReaction {
    JUBILANT,
    ANGRY,
    SAD,
    SILENT
}

enum class SpeakerState {
    SPEAKING, PAUSED, SILENT
}


data class Arthur(
    val action: Action = Action.STAY
) {

    fun fly() = Arthur(Action.FLY)

    fun stay() = Arthur(Action.STAY)
}

data class Window(
    val appearance: WindowAppearance = WindowAppearance.ORDINARY,
    val floor: Int = 1,
    val isOpen: Boolean = false
) {
    fun changeAppearance(newAppearance: WindowAppearance) = Window(newAppearance, floor, isOpen)

    fun changeFloor(newFloor: Int) = Window(appearance, newFloor, isOpen)

    fun openWindow() = Window(appearance, floor, true)

    fun closeWindow() = Window(appearance, floor, false)

    fun isMajestic() = appearance == WindowAppearance.MAJESTIC
}

data class Building(
    val floors: Int = 5,
    val location: Location = Location.BEHIND,
) {
    fun addFloor() = Building(floors + 1, location)

    fun removeFloor() = Building(floors - 1, location)

    fun changeLocation(newLocation: Location) = Building(floors, newLocation)

    fun isTall() = floors > 5
}

data class Platform(
    val location: Location = Location.IN_FRONT_OF,
) {
    fun changeLocation(newLocation: Location) = Platform(newLocation)
}

data class Speaker(
    val state: SpeakerState = SpeakerState.SILENT
) {

    fun startSpeaking() = Speaker(SpeakerState.SPEAKING)

    fun pauseSpeaking() = Speaker(SpeakerState.PAUSED)

    fun stopSpeaking() = Speaker(SpeakerState.SILENT)

    fun isSpeaking() = state == SpeakerState.SPEAKING
}

data class Crowd(
    val people: Int = 0,
    val reaction: CrowdReaction = CrowdReaction.SILENT
) {
    fun addPerson() = Crowd(people + 1, reaction)

    fun isCrowdExist() = people > 0

    private fun changeReaction(newReaction: CrowdReaction) = Crowd(people, newReaction)

    fun isQuiet() = reaction == CrowdReaction.SILENT

    fun reactToSpeaker(speaker: Speaker, reaction: CrowdReaction) =
        (speaker.isSpeaking()).takeIf { it }?.let { changeReaction(reaction) } ?: changeReaction(CrowdReaction.SILENT)
}
