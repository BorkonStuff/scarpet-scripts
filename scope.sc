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
global_single_mode = false;
global_collect_enable = true;

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

probe_color(i) -> (
	global_probe_colors:(i % length(global_probe_colors));
);


// Scale and translate the internal [0,0,0] -> [1,16,21] rendering range to whatever ends up rendered in the world.
p(pos) -> (
	global_scope_pos + pos;
);

rebuild_canvas_shapes() -> (
	global_canvas_shapes = [];
	for(range(global_buf_sz/10 + 1),
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', global_grid_color,
			'from', p([0, 0, _i * 10 * global_render_step]),
			'to', p([0, 16, _i * 10 * global_render_step]),
		]
	);
	for(range(17),
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', global_grid_color,
			'from', p([0,_i, 0]),
			'to', p([0,_i, global_buf_sz * global_render_step]),
		]
	);

	for(values(global_probes),
		global_canvas_shapes += [
			'label', global_canvas_refresh,
			'pos', p([0, 15 - _i, global_buf_sz * global_render_step + 1]),
			'color', probe_color(_i),
			'text', _:'name',
			'size', 15,
		];
		global_canvas_shapes += [
			'line', global_canvas_refresh,
			'color', probe_color(_i),
			'from', p([0, 15 - _i, global_buf_sz * global_render_step + 1]),
			'to', _:'pos' + [0.5,0.5,0.5],
		];
	);
);


// Activate a single 200 (global_buf_sz) tick recording of signals and then stop.
single_recording() -> (
	// Empty all buffers
	for (values(global_probes), _:'buf' = map(_:'buf', 0));
	global_buf_off = 0;
	global_single_mode = true;
);

probe(pos, name) -> (
	b = [];
	for(range(global_buf_sz), b += 0);
	global_probes:name = {
		'pos' -> pos,
		'buf' -> b,
		'name' -> name,
	};
	rebuild_canvas_shapes();
);

stop() -> (
	global_enabled = false;
);

run_probes() -> (
	o = global_buf_off;
	global_buf_off = (o + 1) % global_buf_sz;
	if (global_buf_off == 0 && global_single_mode, global_collect_enable = false);
	for(values(global_probes), _:'buf':o = power(_:'pos'));
);

draw_probes() -> (
	for (values(global_probes),
		draw_probe(_:'buf', global_buf_off, global_probe_colors:(_i % length(global_probe_colors)));
	);
);

draw_probe(pbuf, o, color) -> (
	if (!global_toggle_scroll, o = 0);
	shapes = [];
	lastpos = [-0.1	,pbuf:o,0];
	for(range(length(pbuf)), (
		i = (_i + o) % global_buf_sz;
		pos = [ -0.1, pbuf:i, _i * global_render_step];
		shapes += [
			'line', 1,
			'color', color,
			'from', p(lastpos),
			'to', p(pos),
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
	if (global_collect_enable, run_probes());
	draw_probes();
);

reset() -> (
	global_toggle_scroll = true;
	global_collect_enable = true;
	global_single_mode = false;
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
		'reset' -> 'reset',
		'single' -> 'single_recording',
	},
};
