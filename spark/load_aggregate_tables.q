

insert into table fleet.vehicle_daily_rollup
select fleet_id, day, sum(idle_time) as idle_time, vin
from fleet.vehicle_hist
group by fleet_id, vin, day;


insert into table fleet.fleet_daily_rollup
select fleet_id, day, sum(idle_time) as idle_time
from fleet.vehicle_hist 
group by fleet_id, day;


insert into table fleet.fleet_hourly_rollup
select fleet_id, day, hour, sum(idle_time) as idle_time
from fleet.vehicle_hist
group by fleet_id, day, hour;


insert into table fleet.metrics
select fleet_id, sum(idle_time) as idle_time, count(distinct vin) as vehicles
from fleet.vehicle_hist group by fleet_id;


insert into table fleet.metrics
select 'total' as fleet_id, sum(idle_time) as idle_time, count(distinct vin) as vehicles
from fleet.vehicle_hist;
