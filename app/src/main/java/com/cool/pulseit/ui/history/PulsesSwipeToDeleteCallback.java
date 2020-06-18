package com.cool.pulseit.ui.history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.ui.main.MainActivity;
import com.cool.pulseit.R;

public class PulsesSwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final Context _context = MainActivity.getContext();
    private ColorDrawable _background;
    private Drawable _deleteIcon;
    private PulsesAdapter _adapter;

    public PulsesSwipeToDeleteCallback(PulsesAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        _adapter = adapter;
        _deleteIcon = ContextCompat.getDrawable(_context, R.drawable.ic_delete);
        _background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        _adapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX < 0) { // Swiping to the left
            _background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            _background.setBounds(0, 0, 0, 0);
        }
        _background.draw(c);

        int iconMargin = (itemView.getHeight() - _deleteIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - _deleteIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + _deleteIcon.getIntrinsicHeight();

        if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - _deleteIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            _deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            _background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            _background.setBounds(0, 0, 0, 0);
            _deleteIcon.setBounds(0, 0, 0, 0);
        }

        _background.draw(c);
        _deleteIcon.draw(c);
    }
}
