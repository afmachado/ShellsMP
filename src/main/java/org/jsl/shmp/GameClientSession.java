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

import android.util.Log;
import org.jsl.collider.RetainableByteBuffer;
import org.jsl.collider.Session;
import org.jsl.collider.StreamDefragger;

public class GameClientSession extends GameSession
{
    private final GameClientView m_view;

    public GameClientSession(
            Session session,
            StreamDefragger streamDefragger,
            PingConfig pingConfig,
            GameClientView view )
    {
        super( session, streamDefragger, pingConfig, view );
        m_view = view;
    }

    public int onMessageReceived( int messageID, RetainableByteBuffer msg )
    {
        switch (messageID)
        {
            case Protocol.DragBall.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.DragBall.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.dragBallCT(Protocol.DragBall.getX(msg), Protocol.DragBall.getY(msg));
            break;

            case Protocol.PutBall.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.PutBall.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.putBallCT(Protocol.PutBall.getX(msg), Protocol.PutBall.getY(msg));
            break;

            case Protocol.RemoveBall.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.RemoveBall.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.removeBallCT();
            break;

            case Protocol.DragCap.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.DragCap.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.setCapPositionCT(
                        Protocol.DragCap.getId(msg),
                        Protocol.DragCap.getX(msg),
                        Protocol.DragCap.getY(msg),
                        Protocol.DragCap.getZ(msg) );
            break;

            case Protocol.PutCap.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.PutCap.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.putCapCT(
                        Protocol.PutCap.getId(msg),
                        Protocol.PutCap.getX(msg),
                        Protocol.PutCap.getY(msg),
                        Protocol.PutCap.getGambleTime(msg) );
            break;

            case Protocol.RemoveCap.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.RemoveCap.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.removeCapCT( Protocol.RemoveCap.getId(msg) );
            break;

            case Protocol.Guess.ID:
                if (Log.isLoggable(LOG_PROTOCOL, Log.VERBOSE))
                {
                    final StringBuilder sb = new StringBuilder();
                    Protocol.Guess.print(sb, msg);
                    Log.v(LOG_PROTOCOL, sb.toString());
                }
                m_view.guessCT( Protocol.Guess.getCapWithBall(msg) );
            break;

            default:
                if (BuildConfig.DEBUG)
                    throw new AssertionError();
        }
        return 0;
    }

    public void onConnectionClosed()
    {
        m_view.onServerDisconnected();
        super.onConnectionClosed();
    }
}
