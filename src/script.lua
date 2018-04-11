--利用lua脚本原子性特点来扣减库存达到数据一致的效果
--返回1=抢红包成功，返回0是无库存，返回2是库存告警
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