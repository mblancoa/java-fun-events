package com.funevents.xxxprovider;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.funevents.model.Event;
import com.funevents.xxxprovider.model.BasePlan;
import com.funevents.xxxprovider.model.Output;
import com.funevents.xxxprovider.model.Plan;
import com.funevents.xxxprovider.model.ProviderResponse;
import com.funevents.xxxprovider.model.Zone;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public final class XxxProviderEventMapper {

	private final static String PROVIDER_ID_PATTERN = "%s-%s-%s";

	private final String providerName;

	public List<Event> map(final ProviderResponse response) {
		final List<Event> list = new LinkedList<Event>();
		if (response != null) {
			final Output output = response.getOutput();
			if (output != null) {
				final List<BasePlan> basePlans = output.getBasePlans();
				if (basePlans != null && !basePlans.isEmpty()) {
					for (final BasePlan basePlan : basePlans) {
						final List<Plan> plans = basePlan.getPlans();
						if (plans != null && !plans.isEmpty()) {
							for (final Plan plan : plans) {
								try {
									final Event event = new Event();
									event.setProvId(String.format(PROVIDER_ID_PATTERN, this.providerName,
											basePlan.getBasePlanId(), plan.getPlanId()));
									event.setTitle(basePlan.getTitle());
									event.setOnlineSale("online".equals(basePlan.getSellMode()));
									event.setStartsAt(LocalDateTime.parse(plan.getPlanStartDate()));
									event.setEndsAt(LocalDateTime.parse(plan.getPlanEndDate()));
									final List<Zone> zones = plan.getZones();
									if (zones != null && !zones.isEmpty()) {
										final double maxPrice = zones.stream().map(Zone::getPrice).max(Double::compareTo)
												.orElse(0.0);
										final double minPrice = zones.stream().map(Zone::getPrice).min(Double::compareTo)
												.orElse(0.0);
										event.setMaxPrice(maxPrice);
										event.setMinPrice(minPrice);
									}
									list.add(event);
								} catch (final Exception e) {
									log.debug("Error mapping event: {}\n{}", basePlan, e.getMessage());
									continue;
								}
							}
						}
					}
				}

			}
		}

		return list;

	}
}
