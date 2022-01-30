save_settings() -> (
    write_file('voids_settings', 'JSON', global_settings);
);

global_settings = read_file('voids_settings', 'JSON');

if(global_settings == null,
    global_settings = {
        'dimensions' -> {},
    };
);

_error(msg) -> (
    print(player(), format(str('r %s', msg)));
    exit()
);

add(name,biome) -> (
    dims = get(global_settings, 'dimensions');
    if (has(dims, name), _error('Dimension already exists'));
    dimname = replace(lower(name), ' ', '_');
    create_datapack(dimname, {
        'data' -> { 'minecraft' -> { 'dimension' -> { dimname + '.json' -> { 
            'type' -> 'minecraft:overworld',
	    'generator' -> {
		'type' -> 'minecraft:flat',
		'settings' -> {
		    'layers' -> [],
		    'biome' -> biome,
		    'structures' -> {
			'structures' -> {}
		    }
		}
	    }
    }}}}});
    enable_hidden_dimensions();
    in_dimension(dimname, set(0, 63, 0, block('stone')));   
    put(dims, name, dimname);
    put(global_settings, 'dimensions', dims);
    save_settings();
    print_dim(name, dimname);
);

print_dim(name, dimname) -> (
    print(player(), format(str('d %s', name), str('^mi Teleport to %s', name), str('!/execute in %s run tp %s 0 65 0', dimname, player())))
);

list() -> (
    dims = get(global_settings, 'dimensions');
    print(player(), '-------------');
    for(pairs(dims),
        [name, dimname] = _;
	print_dim(name, dimname);
    );
);

tp(name) -> (
    dims = get(global_settings, 'dimensions');
    dimname = get(dims, name);
    if(dimname == null, _error('Dimension does not exist.'));
    run(str('execute in %s run tp %s 0 65 0', dimname, player()));
);

__config() -> {
    'scope' -> 'global',
    'stay_loaded' -> true,
    'commands' -> {
        'add <name>' -> [ 'add', 'minecraft:plains' ],
	'add <name> <biome>' -> 'add',
        'list' -> 'list',
        'tp <string>' -> 'tp', 
    },
    'arguments' -> {
        'name' -> { 'type' -> 'string' },
        'biome' -> { 'type' -> 'biome', 'sugggest' -> biome() },
    },
};
