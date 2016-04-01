from flask import Flask, render_template
from flask.ext.bootstrap import Bootstrap
from flask.ext.wtf import Form
from wtforms import StringField, IntegerField, SubmitField
from wtforms.validators import Required, Length, Optional
from cassandra.cluster import Cluster

cluster = Cluster(['node0', 'node1', 'node2'])
session = cluster.connect('fleet')

metrics = session.prepare("select * from metrics where fleet_id=?")
fleet_daily = session.prepare("select * from fleet_daily_rollup where fleet_id=?")
fleet_hourly = session.prepare("select * from fleet_hourly_rollup where fleet_id=? and day=?")
vehicle_top = session.prepare("select * from vehicle_daily_rollup where fleet_id=? and day=? order by idle_time desc limit 10")
vehicle_bottom = session.prepare("select * from vehicle_daily_rollup where fleet_id=? and day=? order by idle_time asc limit 10")
vehicle_hist = session.prepare("select * from vehicle_hist where fleet_id=? and vin=? and day=?")

app = Flask(__name__)
app.config['SECRET_KEY'] = 'DATASTAX'
bootstrap = Bootstrap(app)

class QueryForm(Form):
    fleet_id = StringField('fleet_id', validators=[Required(), Length(1, 4)])
    day = IntegerField('day', validators=[Optional()])
    vin = StringField('vin', validators=[Optional()])
    submit = SubmitField('Submit')


@app.route('/', methods=['GET', 'POST'])
def index():
    fleet_data = []
    fleet_top = []
    fleet_bottom = []
    vehicle_data = []
    metrics_rows=[]
    form = QueryForm()
    vehicles=0
    idle=0.0
    if form.validate_on_submit():
        metrics_rows = session.execute(metrics, [form.fleet_id.data])
        if form.day.data:

            if form.vin.data:
                vehicle_rows = session.execute(vehicle_hist, [form.fleet_id.data, form.vin.data, form.day.data])
                for vehicle_row in vehicle_rows:
                    vehicle_data.append(vehicle_row.idle_time)

            fleet_hourly_rows = session.execute(fleet_hourly, [form.fleet_id.data, form.day.data])
            for fleet_hourly_row in fleet_hourly_rows:
                chart_entry = []
                chart_entry.append(fleet_hourly_row.hour)
                chart_entry.append(fleet_hourly_row.idle_time)
                fleet_data.append(chart_entry)

            vehicle_top_rows = session.execute(vehicle_top, [form.fleet_id.data, form.day.data])
            for vehicle_top_row in vehicle_top_rows:
                chart_entry = []
                chart_entry.append(str(vehicle_top_row.vin))
                chart_entry.append(vehicle_top_row.idle_time)
                fleet_top.append(chart_entry)

            vehicle_bottom_rows = session.execute(vehicle_bottom, [form.fleet_id.data, form.day.data])
            for vehicle_bottom_row in vehicle_bottom_rows:
                chart_entry = []
                chart_entry.append(str(vehicle_bottom_row.vin))
                chart_entry.append(vehicle_bottom_row.idle_time)
                fleet_bottom.append(chart_entry)

        else:
            fleet_daily_rows = session.execute(fleet_daily, [form.fleet_id.data])
            for fleet_daily_row in fleet_daily_rows:
                chart_entry = []
                chart_entry.append(fleet_daily_row.day)
                chart_entry.append(fleet_daily_row.idle_time)
                fleet_data.append(chart_entry)
    else:
        metrics_rows = session.execute(metrics, ['total'])

    for metrics_row in metrics_rows:
        vehicles = metrics_row.vehicles;
        idle = metrics_row.idle_time;

    return render_template('index.html', form=form, fleet_data=fleet_data, fleet_top=fleet_top, fleet_bottom=fleet_bottom, vehicles=vehicles, idle=idle, vehicle_data=vehicle_data)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8888, debug=True)

