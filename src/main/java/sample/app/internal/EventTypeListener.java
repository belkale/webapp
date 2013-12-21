package sample.app.internal;

import com.google.common.eventbus.EventBus;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * User: belkale
 * Date: 8/17/13
 */
public class  EventTypeListener implements TypeListener {
    private final EventBus eventBus;

    public EventTypeListener(EventBus eventBus){
        this.eventBus = eventBus;
    }
    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        typeEncounter.register(new InjectionListener<I>() {
            @Override
            public void afterInjection(Object i) {
                eventBus.register(i);
            }
        });
    }
}
