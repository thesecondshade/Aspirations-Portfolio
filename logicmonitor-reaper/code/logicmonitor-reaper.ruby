#!/usr/bin/env ruby

# This script is ran periodically to prune hosts from LogicMonitor service. This
# is especially important as we migrate to AWS and autoscaling becomes standard.

require 'json'
require 'open-uri'
# option parsing
require 'optparse'

options = {}
OptionParser.new do |opt|
  opt.on('-c', '--company company', 'The Company of the account') do |comp|
      options[:company] = comp
  end
  opt.on('-u', '--user user', 'Username with access') do |user|
      options[:user] = user
  end
  opt.on('-p', '--password password', 'Password') do |pass|
      options[:pass] = pass
  end
end.parse!
company = options[:company]
user = options[:user]
pass = options[:pass]
# end option parsing


uri = "https://#{company}.logicmonitor.com/santaba/rpc/getHosts?c=#{company}&u=#{user}&p=#{pass}&hostGroupId=1"
buffer = open(uri).read
data_hash = JSON.parse(buffer)

data_hash['data']['hosts'].each do |host|
    # First check if the host is dead
    # Right now, only removing Shire hosts (10.100.*) and AWS (10.64/10.96)
    if host['status'] == "dead" and host['properties']['system.ips'].match("10\.(100|64|96|66|98|128|129|160|161|192|193|224|225|228|229|255)\.")
        hid = host['id']
        name = host['displayedAs']
        # Get the data from IdleInterval
        uri2 = "https://#{company}.logicmonitor.com/santaba/rpc/getData?c=#{company}&u=#{user}&p=#{pass}&host=#{name}&dataSource=HostStatus&dataPoint0=IdleInterval"
        buffer2 = open(URI::encode(uri2)).read
        data_hash2 = JSON.parse(buffer2)
        time = data_hash2['data']['values']['HostStatus'][0][2]
        if time > 86400 # 1 day
            puts "Not Reporting for > 1 day"
            puts "Host ID: #{hid}"
            puts "#{name}"
            rm = "https://#{company}.logicmonitor.com/santaba/rpc/deleteHost?c=#{company}&u=#{user}&p=#{pass}&hostId=#{hid}&deleteFromSystem=true"
            puts "Running: #{rm}"
            open(rm)
            puts ""
        end
    end
end