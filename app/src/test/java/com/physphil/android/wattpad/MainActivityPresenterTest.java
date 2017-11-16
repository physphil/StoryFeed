package com.physphil.android.wattpad;

import android.support.annotation.NonNull;

import com.physphil.android.wattpad.api.WattpadService;
import com.physphil.android.wattpad.api.model.StoriesApiResponse;
import com.physphil.android.wattpad.feed.presenter.MainActivityPresenter;
import com.physphil.android.wattpad.feed.view.MainActivityView;
import com.physphil.android.wattpad.model.Story;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Copyright (c) 2017 Phil Shadlyn
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Story.class, StoriesApiResponse.class})
public class MainActivityPresenterTest {

    @Mock
    MainActivityView view;

    @Mock
    WattpadService service;

    private MainActivityPresenter presenter;

    @BeforeClass
    public static void setupTestScheduler() {
        // Setup scheduler to return immediately from Observable calls. Otherwise run into concurrency problems.
        // More details here: https://stackoverflow.com/questions/43356314/android-rxjava-2-junit-test-getmainlooper-in-android-os-looper-not-mocked-runt
        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }

            @Override
            public Disposable scheduleDirect(Runnable run, long delay, @NonNull TimeUnit unit) {
                return super.scheduleDirect(run, 0, unit);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        presenter = new MainActivityPresenter(view, service);
    }

    @Test
    public void testLoadStories() throws Exception {
        final List<Story> stories = new ArrayList<>();
        stories.add(PowerMockito.mock(Story.class));
        stories.add(PowerMockito.mock(Story.class));
        stories.add(PowerMockito.mock(Story.class));

        final StoriesApiResponse response = PowerMockito.mock(StoriesApiResponse.class);
        when(response.getStories()).thenReturn(stories);
        when(service.getStories(anyInt(), anyInt())).thenReturn(Observable.just(response));

        presenter.onCreate(null);
        final InOrder order = inOrder(view);

        // Show progress spinner
        order.verify(view).setProgressVisibility(true);
        order.verify(view).setListVisibility(false);
        order.verify(view).setErrorVisibility(false);

        // Show list of stories after success
        order.verify(view).setListVisibility(true);
        order.verify(view).setProgressVisibility(false);
        order.verify(view).setErrorVisibility(false);
        order.verify(view).addStories(stories);
    }

    @Test
    public void testNoStoriesLoaded() throws Exception {
        final List<Story> stories = new ArrayList<>();

        final StoriesApiResponse response = PowerMockito.mock(StoriesApiResponse.class);
        when(response.getStories()).thenReturn(stories);
        when(service.getStories(anyInt(), anyInt())).thenReturn(Observable.just(response));

        presenter.onCreate(null);
        final InOrder order = inOrder(view);

        // Show progress spinner
        order.verify(view).setProgressVisibility(true);
        order.verify(view).setListVisibility(false);
        order.verify(view).setErrorVisibility(false);

        // Show empty view when no stories are returned
        order.verify(view).setErrorMessage(R.string.empty_stories_list);
        order.verify(view).setErrorVisibility(true);
        order.verify(view).setProgressVisibility(false);
        order.verify(view).setListVisibility(false);
    }

    @Test
    public void testErrorLoadingStories() throws Exception {
        when(service.getStories(anyInt(), anyInt())).thenReturn(Observable.<StoriesApiResponse>error(new Exception("BOOM!!!")));

        presenter.onCreate(null);
        final InOrder order = inOrder(view);

        // Show progress spinner
        order.verify(view).setProgressVisibility(true);
        order.verify(view).setListVisibility(false);
        order.verify(view).setErrorVisibility(false);

        // Show error state
        order.verify(view).setErrorMessage(R.string.error_loading_stories);
        order.verify(view).setErrorVisibility(true);
        order.verify(view).setProgressVisibility(false);
        order.verify(view).setListVisibility(false);
    }
}
