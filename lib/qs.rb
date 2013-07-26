class QS < Sinatra::Application

  get '/qs/:api' do |api|
    api
  end    
      
  def self.query_api(options)
    api_map[options[:api]] = options[:query] 
  end
  
  def self.api_map
    @api_map ||= {}
  end
  
end