global_c = [0,0,0];

global_pscanx = 0;
global_pscanz = 0;
global_pscan_max = 16;

f(pos,b) -> (
	[x,y,z] = pos;
	xmin = x - (x%16);
	zmin = z - (z%16);

	scan(xmin, -9, zmin, 0, 0, 0, 15, 0, 15, set(_, b));
);

chunk_test(pos) -> (
	[c,cap] = get_mob_counts('monster');
	if (c==0, f(pos, 'white_concrete'), f(pos, 'red_concrete'));
);

set_center(pos) -> (
	global_c = pos;
);

tp_next() -> (
	global_pscanx = global_pscanx + 1;
	if (global_pscanx == global_pscan_max, global_pscanx = -global_pscan_max; global_pscanz = global_pscanz + 1);
	modify(player(), 'pos', global_c + [global_pscanx * 16, 0, global_pscanz * 16]);
);

pscan_next_chunk() -> (
	if (global_pscanz == global_pscan_max, return());
	chunk_test(global_c + [global_pscanx * 16, 0, global_pscanz * 16]);
	tp_next();
	schedule(5, 'pscan_next_chunk');
);

pscan(chunks) -> (
	global_pscan_max = chunks;
	global_pscanx = -chunks - 1;
	global_pscanz = -chunks - 1;
	tp_next();
	schedule(5, 'pscan_next_chunk');
);

__config() -> {
    'commands' -> {
        'c <pos>' -> 'chunk_test',
        'center <pos>' -> 'set_center',
	'pscan <int>' -> 'pscan',
    }
};