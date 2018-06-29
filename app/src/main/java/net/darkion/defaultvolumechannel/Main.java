package net.darkion.defaultvolumechannel;

import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.view.KeyEvent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class Main implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        try {
            XposedHelpers.findAndHookMethod(MediaSessionManager.class, "dispatchAdjustVolume", int.class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            if ((int) param.args[0] == AudioManager.USE_DEFAULT_STREAM_TYPE || (int) param.args[0] == AudioManager.STREAM_RING) {
                                param.args[0] = AudioManager.STREAM_MUSIC;
                            }

                        }
                    }
            );
        } catch (Exception e) {
            //suppress
        }

        if (Build.VERSION.SDK_INT >= 26)
            try {
                XposedHelpers.findAndHookMethod(MediaSessionManager.class, "dispatchVolumeKeyEvent", KeyEvent.class, int.class, boolean.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                if ((int) param.args[1] == AudioManager.USE_DEFAULT_STREAM_TYPE || (int) param.args[1] == AudioManager.STREAM_RING) {
                                    param.args[1] = AudioManager.STREAM_MUSIC;
                                }

                            }
                        }
                );
            } catch (Exception e) {
                //suppress
            }

        try {
            final Class<?> MediaSessionLegacyHelper = XposedHelpers.findClass("android.media.session.MediaSessionLegacyHelper", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(MediaSessionLegacyHelper, "sendAdjustVolumeBy", int.class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            if ((int) param.args[0] == AudioManager.USE_DEFAULT_STREAM_TYPE || (int) param.args[0] == AudioManager.STREAM_RING) {
                                param.args[0] = AudioManager.STREAM_MUSIC;
                            }
                        }
                    }
            );
        } catch (Exception e) {
            //suppress
        }

    }


}
