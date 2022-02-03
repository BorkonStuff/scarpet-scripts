global_scope_pos = [0,0,0];
global_enabled = false;
global_dim = null;

global_render_step = 0.1;
global_buffer_sz = 200;

global_probes = {};

global_canvas_shapes = [];
global_canvas_refresh = 20;

global_probe_colors = [
	0xff0000ff,
	0x00ff00ff,
	0x0000ffff,
	0xff00ffff,
	0xffff00ff,
	0x00ffffff,
];
	
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

probe(pos, name) -> (
	p = {
		'pos' -> pos,
		'buf' -> [0],
		'off' -> 0,
		'name' -> name,
	};
	put(global_probes, name, p);
);

stop() -> (
	global_enabled = false;
);

run_probes() -> (
	for(values(global_probes), (
		p = power(get(_, 'pos'));
		o = get(_, 'off');
		put(get(_, 'buf'), o, p);
		o = (o + 1) % global_buffer_sz;
		put(_, 'off', o);
		draw_probe(get(_, 'buf'), o, get(global_probe_colors, _i % length(global_probe_colors)));
	));
);

draw_probe(pbuf, o, color) -> (
	shapes = [];
	lastpos = [-0.1	,get(pbuf, o),0];
	for(range(length(pbuf)), (
		i = (_i + o) % global_buffer_sz;
		v = get(pbuf, i);
		pos = [ -0.1, v, _i * global_render_step];
		shapes += [
			'line', 1,
			'color', color,
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
		'probe <pos> <name>' -> 'probe',
	},
};
