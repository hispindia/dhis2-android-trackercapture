package org.hisp.dhis.android.trackercapture.ui.rows.programoverview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hisp.dhis.android.sdk.events.OnRowClick;
import org.hisp.dhis.android.sdk.events.OnTrackerItemClick;
import org.hisp.dhis.android.sdk.persistence.Dhis2Application;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.hisp.dhis.android.trackercapture.R;
import org.hisp.dhis.android.trackercapture.ui.rows.selectprogram.TrackedEntityInstanceRow;
import org.hisp.dhis.android.trackercapture.ui.rows.upcomingevents.EventRowType;

import static org.hisp.dhis.android.sdk.utils.Preconditions.isNull;

/**
 * Created by erling on 5/11/15.
 */
public class SearchRelativeTrackedEntityInstanceItemRow implements TrackedEntityInstanceRow
{
    private TrackedEntityInstance mTrackedEntityInstance;
    private String mFirstItem;
    private String mSecondItem;
    private String mThirdItem;
    private String mFourthItem;

    public SearchRelativeTrackedEntityInstanceItemRow(Context context)
    {
        isNull(context, "Context must not be null");
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = inflater.inflate(org.hisp.dhis.android.sdk.R.layout.listview_trackedentityinstance_item, container, false);
            holder = new ViewHolder(
                    (TextView) view.findViewById(R.id.first_event_item),
                    (TextView) view.findViewById(R.id.second_event_item),
                    (TextView) view.findViewById(R.id.third_event_item),
                    (TextView) view.findViewById(R.id.status_text_view)
            );
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.firstItem.setText(mFirstItem);
        holder.secondItem.setText(mSecondItem);
        holder.thirdItem.setText(mThirdItem);
        holder.fourthItem.setText(mFourthItem);

        return view;
    }

    @Override
    public int getViewType() {
        return EventRowType.EVENT_ITEM_ROW.ordinal();
    }

    @Override
    public long getId() {
        if (mTrackedEntityInstance != null) {
            return mTrackedEntityInstance.getLocalId();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public SearchRelativeTrackedEntityInstanceItemRow getItemRow() {
        return this;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        mTrackedEntityInstance = trackedEntityInstance;
    }

    public void setSecondItem(String secondItem) {
        this.mSecondItem = secondItem;
    }

    public void setThirdItem(String thirdItem) {
        this.mThirdItem = thirdItem;
    }

    public void setFirstItem(String firstItem) {
        this.mFirstItem = firstItem;
    }

    public String getmSecondItem() {
        return mSecondItem;
    }

    public String getmThirdItem() {
        return mThirdItem;
    }

    public String getmFirstItem() {
        return mFirstItem;
    }

    public String getmFourthItem() {
        return mFourthItem;
    }

    public void setFourthItem(String mFourthItem) {
        this.mFourthItem = mFourthItem;
    }

    private static class ViewHolder {
        public final TextView firstItem;
        public final TextView secondItem;
        public final TextView thirdItem;
        public final TextView fourthItem;

        private ViewHolder(TextView firstItem,
                           TextView secondItem,
                           TextView thirdItem,
                           TextView fourthItem) {
            this.firstItem = firstItem;
            this.secondItem = secondItem;
            this.thirdItem = thirdItem;
            this.fourthItem = fourthItem;
        }
    }
}
