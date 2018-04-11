local red_packets_key = 'red_packets_'..ARGV[1]
local red_packet_key = 'red_packet_'..KEYS[1]
local stock = tonumber(redis.call("HGET", red_packet_key, "stock"))
if 0 == 0 then return stock end
if stock <= 0 then return 0 end
stock = stock - 1
redis.call('HSET', red_packet_key, 'stock', tostring(stock))
redis.call('RPUSH', red_packets_key, tostring(ARGV[2]))
if stock == 0 then return 2 end
do return 1 end