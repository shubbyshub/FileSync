## Sync
Very useful if you own more than one server and want to run a command to the other server without switching between servers.
The commands are run as console.

### Dependency
This plugin requires SynX: http://github.com/KaiKikuchi/SynX

### Download
All builds for my plugins can be found at this link: http://kaikk.net/mc/

### How to use
The main and only command is /sync, followed by a comma-separated list of servers, tags, the keyword "all", and/or settings, followed by the command.

#### Tags
Servers may have one or more tags specified on their config.yml files. This will allow to send commands to all servers with a specified tag instead of specifying each server every time. Tags starts with a #

#### Settings
All settings starts with a ! and they're specified on the first comma-separated parameter.  
This is a list of settings you can use:
- sm = Suppress message: no response will be produced by the server
- sc = Server console: the console will receive the response instead
- tl# = Time to live: this command won't be executed on the target server after the specified amount of seconds
- ep = Emulate player: this should trick the server that receives the command making the plugin think a player is running this command
- vv = Verbose: logs info on the server console about this command.

#### Examples:
- `/sync lobby list` - this will return the online players' list on "lobby"
- `/sync lobby,#vanilla list` - this will return the online players' list on "lobby" and all servers with the tag "vanilla"
- `/sync all,!sc,!tl60 list` - all known servers will show the online players' list on those server console, but only if those servers receive this command in 60 seconds (consider if the target server is currently off and will be started after a minute)

#### Permissions
- sync.use - Permits all commands in all servers (default: op)
- sync.reload - Reload the sync plugin (default: op)

