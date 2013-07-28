class PGHelper
  def self.connect
    PG.connect(
      :host => ENV['QS_HOST'],
      :port => ENV['QS_PORT'],
      :dbname => ENV['QS_DBNAME'],
      :user => ENV['QS_USER'],
      :password => ENV['QS_PASSWORD'])
  end

  def self.query_as_json(query)
    conn = connect    
    
    rows = conn.exec(query) do |result|
      result.inject([]) do |rows, row|
        rows << row
      end      
    end    
    
    rows.to_json
  end
   
end