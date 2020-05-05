/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greenrobot.eventbus;

/**
 * Each subscriber method has a thread mode, which determines in which thread the method is to be called by EventBus.
 * EventBus takes care of threading independently from the posting thread.
 * 
 * @see EventBus#register(Object)
 * @author Markus
 */
public enum ThreadMode {
    /**
     * Subscriber will be called directly in the same thread, which is posting the event. This is the default. Event delivery
     * implies the least overhead because it avoids thread switching completely. Thus this is the recommended mode for
     * simple tasks that are known to complete in a very short time without requiring the main thread. Event handlers
     * using this mode must return quickly to avoid blocking the posting thread, which may be the main thread.
     *
     * 默认的线程模式，在那个线程发送事件就在对应线程处理事件，避免了线程切换，效率高
     */
    POSTING,

    /**
     * On Android, subscriber will be called in Android's main thread (UI thread). If the posting thread is
     * the main thread, subscriber methods will be called directly, blocking the posting thread. Otherwise the event
     * is queued for delivery (non-blocking). Subscribers using this mode must return quickly to avoid blocking the main thread.
     * If not on Android, behaves the same as {@link #POSTING}.
     *
     * 如在主线程发送事件，则直接在主线程处理事件，如果在子线程发送事件，则先将事件入队列，然后通过Handler切换到主线程，依次处理事件
     */
    MAIN,

    /**
     * On Android, subscriber will be called in Android's main thread (UI thread). Different from {@link #MAIN},
     * the event will always be queued for delivery. This ensures that the post call is non-blocking.
     *
     * 无论在那个线程发送事件，都先将事件入队列，然后通过Handler切换到主线程，依次处理事件
     */
    MAIN_ORDERED,

    /**
     * On Android, subscriber will be called in a background thread. If posting thread is not the main thread, subscriber methods
     * will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single
     * background thread, that will deliver all its events sequentially. Subscribers using this mode should try to
     * return quickly to avoid blocking the background thread. If not on Android, always uses a background thread.
     *
     * 如果在主线程发送事件，则先将事件入队列，然后通过线程池依次处理事件，如果在子线程发送事件，则直接在发送事件的线程处理事件
     */
    BACKGROUND,

    /**
     * Subscriber will be called in a separate thread. This is always independent from the posting thread and the
     * main thread. Posting events never wait for subscriber methods using this mode. Subscriber methods should
     * use this mode if their execution might take some time, e.g. for network access. Avoid triggering a large number
     * of long running asynchronous subscriber methods at the same time to limit the number of concurrent threads. EventBus
     * uses a thread pool to efficiently reuse threads from completed asynchronous subscriber notifications.
     * 无论在那个线程发送事件，都将事件入队列，然后通过线程池处理
     */
    ASYNC
}