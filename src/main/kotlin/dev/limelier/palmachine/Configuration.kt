package dev.limelier.palmachine

import com.sksamuel.hoplite.Masked

data class Configuration(
    val db: Database,
    val guild: Guild,
) {
    data class Database(
        val connectionString: String,
        val user: String,
        val password: Masked,
    )

    data class Guild(
        val id: String?,
        val enrolledRoleId: String?,
        val activeRoleId: String?,
    )
}

