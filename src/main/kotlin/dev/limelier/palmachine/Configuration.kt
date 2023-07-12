package dev.limelier.palmachine

data class Configuration(
    val guild: Guild,
) {
    data class Guild(
        val id: String?,
        val enrolledRoleId: String?,
        val activeRoleId: String?,
    )
}
