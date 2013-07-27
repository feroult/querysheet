class QSSinatra < Sinatra::Application  
  
  get '/qs/*' do |api|
    qs.execute_query(api)
  end    
  
  def qs
    @@qs ||= QS.new
  end
    
  def self.qs=(qs)
    @@qs = qs
  end
  
end