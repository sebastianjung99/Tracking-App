package Other

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class Gesture(): ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
       // nothing
    }

}