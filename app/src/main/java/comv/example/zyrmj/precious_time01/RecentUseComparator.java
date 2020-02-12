package comv.example.zyrmj.precious_time01;

import android.app.usage.UsageStats;

import java.util.Comparator;

public class RecentUseComparator implements Comparator<UsageStats> {
    @Override
    public int compare(UsageStats lhs , UsageStats rhs) {
        return (lhs.getLastTimeUsed () > rhs.getLastTimeUsed ()) ? -1 : (lhs.getLastTimeUsed () == rhs.getLastTimeUsed ()) ? 0 : 1;
    }
}
