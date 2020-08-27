package abi39_0_0.host.exp.exponent.modules.api.reanimated;

import androidx.annotation.Nullable;

import com.facebook.jni.HybridData;
import com.facebook.proguard.annotations.DoNotStrip;
import abi39_0_0.com.facebook.react.bridge.JSIModule;
import abi39_0_0.com.facebook.react.bridge.ReactApplicationContext;
import abi39_0_0.com.facebook.react.bridge.WritableArray;
import abi39_0_0.com.facebook.react.bridge.WritableMap;
import abi39_0_0.com.facebook.react.turbomodule.core.interfaces.TurboModule;
import abi39_0_0.com.facebook.react.turbomodule.core.interfaces.TurboModuleRegistry;
import abi39_0_0.com.facebook.react.uimanager.UIManagerModule;
import abi39_0_0.com.facebook.react.uimanager.events.RCTEventEmitter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class NativeProxy {

  static {
    System.loadLibrary("reanimated_abi39_0_0");
  }

  @DoNotStrip
  public static class AnimationFrameCallback implements NodesManager.OnAnimationFrame {

    @DoNotStrip
    private final HybridData mHybridData;

    @DoNotStrip
    private AnimationFrameCallback(HybridData hybridData) {
      mHybridData = hybridData;
    }

    @Override
    public native void onAnimationFrame(double timestampMs);
  }

  @DoNotStrip
  public static class EventHandler implements RCTEventEmitter {

    @DoNotStrip
    private final HybridData mHybridData;
    private UIManagerModule.CustomEventNamesResolver mCustomEventNamesResolver;

    @DoNotStrip
    private EventHandler(HybridData hybridData) {
      mHybridData = hybridData;
    }

    @Override
    public void receiveEvent(int targetTag, String eventName, @Nullable WritableMap event) {
      String resolvedEventName = mCustomEventNamesResolver.resolveCustomEventName(eventName);
      receiveEvent(targetTag + resolvedEventName, event);
    }

    public native void receiveEvent(String eventKey, @Nullable WritableMap event);

    @Override
    public void receiveTouches(String eventName, WritableArray touches, WritableArray changedIndices) {
      // not interested in processing touch events this way, we process raw events only
    }
  }

  @DoNotStrip
  @SuppressWarnings("unused")
  private final HybridData mHybridData;
  private NodesManager mNodesManager;
  private final WeakReference<ReactApplicationContext> mContext;

  public NativeProxy(ReactApplicationContext context) {
    mHybridData = initHybrid(context.getJavaScriptContextHolder().get(), new Scheduler(context));
    mContext = new WeakReference<>(context);
    prepare();
  }

  private native HybridData initHybrid(long jsContext, Scheduler scheduler);
  private native void installJSIBindings();

  @DoNotStrip
  private void requestRender(AnimationFrameCallback callback) {
    mNodesManager.postOnAnimation(callback);
  }

  @DoNotStrip
  private void updateProps(int viewTag, Map<String, Object> props) {
    mNodesManager.updateProps(viewTag, props);
  }

  @DoNotStrip
  private String obtainProp(int viewTag, String propName) {
     return mNodesManager.obtainProp(viewTag, propName);
  }

  @DoNotStrip
  private void scrollTo(int viewTag, double x, double y, boolean animated) {
    mNodesManager.scrollTo(viewTag, x, y, animated);
  }

  @DoNotStrip
  private float[] measure(int viewTag) {
    return mNodesManager.measure(viewTag);
  }

  @DoNotStrip
  private void registerEventHandler(EventHandler handler) {
    handler.mCustomEventNamesResolver = mNodesManager.getEventNameResolver();
    mNodesManager.registerEventHandler(handler);
  }

  public void onCatalystInstanceDestroy() {
    mHybridData.resetNative();
  }

  public void prepare() {
    mNodesManager = mContext.get().getNativeModule(ReanimatedModule.class).getNodesManager();
    installJSIBindings();
  }
}
