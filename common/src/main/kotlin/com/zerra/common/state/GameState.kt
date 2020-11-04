package com.zerra.common.state

import com.zerra.common.api.state.State
import com.zerra.common.realm.Universe

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class GameState: State {
    val universe = Universe("NULL")
}