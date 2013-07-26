require 'rubygems'
require 'bundler/setup'
require 'sinatra'
require 'pg'
 
require './lib/hello'
require './lib/hello2'  
 
set :run, false
set :raise_errors, true
 
run Sinatra::Application
