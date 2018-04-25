package debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.View;

import tech.summerly.quiet.commonlib.base.BaseActivity;
import tech.summerly.quiet.commonlib.utils.LoggerKt;
import tech.summerly.quiet.commonlib.utils.LoggerLevel;
import tech.summerly.quiet.search.R;
import tech.summerly.quiet.search.fragments.items.SearchHotHintKt;
import tech.summerly.quiet.search.utils.ChipsLayout;

public class DebugActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_item_chips);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ChipsLayout chipsLayout = findViewById(R.id.chipLayout);

        for (int i = 0; i < 10; i++) {
            chipsLayout.addView(chipsLayout);
        }

        for (int i = 0; i < chipsLayout.getChildCount(); i++) {
            View child = chipsLayout.getChildAt(i);
            TransitionManager.beginDelayedTransition(chipsLayout);
            child.setVisibility(View.GONE);
        }
    }

    private View getView(final ChipsLayout parent) {
        return SearchHotHintKt.generateChipItem("匿藏", parent);
    }
}
