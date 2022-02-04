global_scope_pos = [0,0,0];
global_enabled = false;
global_dim = null;

global_render_step = 0.1;
global_buf_sz = 200;
global_buf_off = 0;

global_probes = {};

global_canvas_shapes = [];
global_canvas_refresh = 20;

global_toggle_scroll = true;

global_grid_color = 0x0000009f;

global_probe_colors = [
	0xff00009f,
	0x00ff009f,
	0x0000ff9f,
	0xff00ff9f,
	0xffff009f,
	0x00ffff9f,
];
	
create(pos) -> (
	global_scope_pos = pos + [-0.1,0,0];
	global_enabled = true;
	global_dim = current_dimension();

	rebuild_canvas_shapes();
);

rebuild_canvas_shapes() -> (
	global_canvas_shapes = [];
	for(range(global_buf_sz/10 + 1),
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', global_grid_color,
			'from', global_scope_pos + [0, 0, _i * 10 * global_render_step],
			'to', global_scope_pos + [0, 16, _i * 10 * global_render_step],
		]
	);
	for(range(17),
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', global_grid_color,
			'from', global_scope_pos + [0,_i, 0],
			'to', global_scope_pos + [0,_i, global_buf_sz * global_render_step],
		]
	);

	for(values(global_probes),
		global_canvas_shapes += [
			'label', global_canvas_refresh,
			'pos', global_scope_pos + [0, 15 - _i, global_buf_sz * global_render_step + 1],
			'color', get(global_probe_colors, _i % length(global_probe_colors)),
			'text', get(_, 'name'),
			'size', 15,
		];
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', get(global_probe_colors, _i % length(global_probe_colors)),
			'from', global_scope_pos + [0, 15 - _i, global_buf_sz * global_render_step + 1],
			'to', get(_, 'pos'),
		];
	);
);

probe(pos, name) -> (
	b = [];
	for(range(global_buf_sz), b += 0);
	p = {
		'pos' -> pos,
		'buf' -> b,
		'name' -> name,
	};
	put(global_probes, name, p);
	rebuild_canvas_shapes();
);

stop() -> (
	global_enabled = false;
);

run_probes() -> (
	o = global_buf_off;
	global_buf_off = (o + 1) % global_buf_sz;
	for(values(global_probes), (
		p = power(get(_, 'pos'));
		put(get(_, 'buf'), o, p);
		draw_probe(get(_, 'buf'), global_buf_off, get(global_probe_colors, _i % length(global_probe_colors)));
	));
);

draw_probe(pbuf, o, color) -> (
	if (!global_toggle_scroll, o = 0);
	shapes = [];
	lastpos = [-0.1	,get(pbuf, o),0];
	for(range(length(pbuf)), (
		i = (_i + o) % global_buf_sz;
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

toggle_scroll() -> (
	global_toggle_scroll = !global_toggle_scroll;
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
		'toggle_scroll' -> 'toggle_scroll',
	},
};
