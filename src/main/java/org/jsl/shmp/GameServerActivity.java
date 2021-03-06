/*
 * Copyright (C) 2015 Sergey Zubarev, info@js-labs.org
 *
 * This file is a part of ShellsMP application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jsl.shmp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GameServerActivity extends GameActivity
{
    private static final String LOG_TAG = GameServerActivity.class.getSimpleName();

    private GameServerView m_view;
    private boolean m_pause;

    public GameServerActivity()
    {
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String deviceId = (String) getIntent().getSerializableExtra( MainActivity.EXTRA_DEVICE_ID );
        final String playerName = (String) getIntent().getSerializableExtra( MainActivity.EXTRA_PLAYER_NAME );
        final Short gameTime = (Short) getIntent().getSerializableExtra( MainActivity.EXTRA_GAME_TIME );
        final Short caps = (Short) getIntent().getSerializableExtra( MainActivity.EXTRA_CAPS );

        final NsdManager nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        final SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        final boolean renderShadows = sharedPreferences.getBoolean(Prefs.RENDER_SHADOWS, true);

        Log.d(LOG_TAG, "onCreate: deviceId=[" + deviceId +
                "] playerName=[" + playerName + "] gameTime=" + gameTime + " caps=" + caps);

        m_view = new GameServerView(this, deviceId, playerName, nsdManager, renderShadows, gameTime, caps);
        setContentView(m_view);

        m_pause = false;
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public void onResume()
    {
        super.onResume();
        Log.d( LOG_TAG, "onResume" );

        if (m_pause)
        {
            /* Activity was paused and now resumed,
             * game is finished anyway, return to the main activity.
             */
            finish();
        }
        else
            m_view.onResume();
    }

    public void onBackPressed()
    {
        Log.d( LOG_TAG, "onBackPressed" );
        if (!m_pause)
        {
            /* Activity will not be started any more */
            m_pause = true;
            final Intent result = m_view.onPauseEx();
            if (result != null)
                setResult(0, result);
        }
        super.onBackPressed();
    }

    public void onPause()
    {
        Log.d( LOG_TAG, "onPause" );
        if (!m_pause)
        {
            /* Activity will not be started any more */
            m_pause = true;
            final Intent result = m_view.onPauseEx();
            if (result != null)
                setResult(0, result);
        }
        super.onPause();
    }

    public void onGameRegistrationFailed()
    {
        runOnUiThread( new Runnable() {
            public void run() {
                finish();
            }
        } );
    }
}
