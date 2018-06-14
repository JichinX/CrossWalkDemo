package me.xujichang.crosswalksdk.ability.location;

import me.xujichang.crosswalksdk.ability.IAbilityCallBack;
import me.xujichang.crosswalksdk.base.IWrapperContext;
import me.xujichang.crosswalksdk.bean.LocationType;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/14 - 13:50
 */
public class LocationAbility {
    /**
     * 处理结果回调 无默认值
     */
    private IAbilityCallBack mIAbilityCallBack;
    /**
     * 上下文 Context
     */
    private IWrapperContext mContext;
    /**
     * 位置请求类型
     */
    private LocationType mType;

    private LocationAbility(Builder builder) {
        mIAbilityCallBack = builder.mIAbilityCallBack;
        mContext = builder.mContext;
        mType = builder.mType;
    }

    public void execute() {
        mContext.onGotLocation(mIAbilityCallBack, mType);
    }

    public static final class Builder {
        private IAbilityCallBack mIAbilityCallBack;
        private IWrapperContext mContext;
        private LocationType mType;

        public Builder() {
        }

        public Builder mIAbilityCallBack(IAbilityCallBack val) {
            mIAbilityCallBack = val;
            return this;
        }

        public Builder mContext(IWrapperContext val) {
            mContext = val;
            return this;
        }

        public Builder mType(LocationType val) {
            mType = val;
            return this;
        }

        public LocationAbility build() {
            return new LocationAbility(this);
        }
    }
}
