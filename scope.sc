global_scope_pos = [0,0,0];
global_enabled = false;
global_dim = null;

global_render_step = 0.1;
global_buffer_sz = 200;

global_probe = [0,0,0];
global_probe_buf = [];

global_canvas_shapes = [];
global_canvas_refresh = 20;
	
create(pos) -> (
	global_scope_pos = pos + [-0.1,0,0];
	global_enabled = true;
	global_dim = current_dimension();

	global_canvas_shapes = [];
	for(range(global_buffer_sz/10 + 1),
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', 0x000000ff,
			'from', global_scope_pos + [0, 0, _i * 10 * global_render_step],
			'to', global_scope_pos + [0, 16, _i * 10 * global_render_step],
		]
	);
	for(range(17),
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', 0x000000ff,
			'from', global_scope_pos + [0,_i, 0],
			'to', global_scope_pos + [0,_i, global_buffer_sz * global_render_step],
		]
	);
);

probe(pos) -> (
	global_probe = pos;
);

stop() -> (
	global_enabled = false;
);

run_probes() -> (
	p = power(global_probe);
	if (length(global_probe_buf) == global_buffer_sz, delete(global_probe_buf,0));
	global_probe_buf += p;
);

draw_probe(pbuf) -> (
	shapes = [];
	lastpos = [-0.1	,get(pbuf, 0),0];
	for(pbuf, (
		pos = [ -0.1, _, _i * global_render_step];
		shapes += [
			'line', 1,
			'color', 0xff0000ff,
			'from', global_scope_pos + lastpos,
			'to', global_scope_pos + pos,
		];
		lastpos = pos;
	));
	draw_shape(shapes);
);

do_draw() -> (
	if ((tick_time() % global_canvas_refresh) == 0, draw_shape(global_canvas_shapes));
	run_probes();
	draw_probe(global_probe_buf);
);

__on_tick() -> (
	if (!global_enabled, return());
	in_dimension(global_dim, do_draw());
);

__config() -> {
	'scope' -> 'player',
	'stay_loaded' -> true,
	'commands' -> {
		'pos <pos>' -> 'create',
		'stop' -> 'stop',
		'probe <pos>' -> 'probe',
	},
};

