/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.core.object.entity;

import discord4j.core.ServiceMediator;
import discord4j.core.object.data.stored.CategoryBean;
import discord4j.core.object.trait.Categorizable;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.CategoryEditSpec;
import discord4j.core.util.EntityUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/** A Discord category. */
public final class Category extends BaseGuildChannel {

    /**
     * Constructs an {@code Category} with an associated ServiceMediator and Discord data.
     *
     * @param serviceMediator The ServiceMediator associated to this object, must be non-null.
     * @param data The raw data as represented by Discord, must be non-null.
     */
    public Category(final ServiceMediator serviceMediator, final CategoryBean data) {
        super(serviceMediator, data);
    }

    /**
     * Requests to retrieve the channels residing in this category.
     *
     * @return A {@link Flux} that continually emits the {@link GuildChannel channels} residing in this category. If an
     * error is received, it is emitted through the {@code Flux}.
     */
    public Flux<GuildChannel> getChannels() {
        return getGuild().flatMapMany(Guild::getChannels)
                .ofType(Categorizable.class)
                .filter(channel -> channel.getCategoryId().orElse(Snowflake.of(0)).equals(getId()))
                .cast(GuildChannel.class);
    }

    /**
     * Requests to edit this category.
     *
     * @param spec A {@link Consumer} that provides a "blank" {@link CategoryEditSpec} to be operated on. If some
     * properties need to be retrieved via blocking operations (such as retrieval from a database), then it is
     * recommended to build the spec externally and call {@link #edit(CategoryEditSpec)}.
     *
     * @return A {@link Mono} where, upon successful completion, emits the edited {@link Category}. If an error is
     * received, it is emitted through the {@code Mono}.
     */
    public Mono<Category> edit(final Consumer<CategoryEditSpec> spec) {
        final CategoryEditSpec mutatedSpec = new CategoryEditSpec();
        spec.accept(mutatedSpec);
        return edit(mutatedSpec);
    }

    /**
     * Requests to edit this category.
     *
     * @param spec A configured {@link CategoryEditSpec} to perform the request on.
     * @return A {@link Mono} where, upon successful completion, emits the edited {@link Category}. If an error is
     * received, it is emitted through the {@code Mono}.
     */
    public Mono<Category> edit(final CategoryEditSpec spec) {
        return getServiceMediator().getRestClient().getChannelService()
                .modifyChannel(getId().asLong(), spec.asRequest())
                .map(EntityUtil::getChannelBean)
                .map(bean -> EntityUtil.getChannel(getServiceMediator(), bean))
                .cast(Category.class);
    }

    @Override
    public String toString() {
        return "Category{} " + super.toString();
    }
}
