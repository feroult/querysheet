require 'rubygems'
require 'bundler/setup'
require 'sinatra'
require 'pg'
 
Dir["./lib/*.rb"].each {|file| require file }

set :run, false
set :raise_errors, true
 
run Sinatra::Application
