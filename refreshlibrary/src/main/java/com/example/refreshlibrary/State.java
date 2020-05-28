package com.example.refreshlibrary;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @ProjectName: Refresh
 * @Package: com.example.refreshlibrary
 * @ClassName: State
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2020/5/28 17:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/28 17:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@IntDef({
        State.REFRESHING,
        State.NORMAL,
        State.LOADING
})
@Retention(RetentionPolicy.SOURCE)
public @interface State {
    int REFRESHING = -1;
    int NORMAL = 0;
    int LOADING = 1;
}
