class QSSinatra < Sinatra::Application  
  
  get '/qs/*' do |api|
    content_type :json
    qs.execute_query(api)
  end    
  
  def qs
    @@qs ||= QS.new
  end
    
  def self.qs=(qs)
    @@qs = qs
  end
  
end