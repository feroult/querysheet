class QSSinatra < Sinatra::Application
  
  get '/qs/*' do |api|
    content_type :json    
    return not_found if qs.invalid?(api)    
    qs.execute_query(api)
  end

  def not_found
    response.status = 400
    ''
  end
  
  def qs
    @@qs ||= QS.new
  end

  def self.qs=(qs)
    @@qs = qs
  end

  private :qs
end